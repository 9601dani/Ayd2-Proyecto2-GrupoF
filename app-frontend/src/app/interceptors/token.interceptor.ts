import {HttpErrorResponse, HttpEvent, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {LocalStorageService} from '../services/commons/local-storage.service';
import {Router} from '@angular/router';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {AlertService} from '../services/commons/alert.service';
import {UserService} from '../services/user/user.service';

export const tokenInterceptor: HttpInterceptorFn = (req, next): Observable<HttpEvent<any>> => {

  const _localStorageService: LocalStorageService = inject(LocalStorageService);
  const _cookieService: CookieService = inject(CookieService);
  const _userService: UserService = inject(UserService);
  const _alertService: AlertService = inject(AlertService);
  const _router: Router = inject(Router);

  const request = req.clone({
    setHeaders: {
      Authorization: `Bearer ${_cookieService.get(_localStorageService.TOKEN)}`
    }
  });

  return next(request).pipe(
    catchError((error: any) => {
      if(error instanceof HttpErrorResponse) {
        const { message } = error.error;
        if(error.status === 401) {
          const refreshToken = _cookieService.get(_localStorageService.REFRESH_TOKEN);
          const userId = _localStorageService.getItem(_localStorageService.USER_ID);

          console.log(userId, refreshToken);
          return _userService.refreshToken(userId, refreshToken).pipe(
            switchMap((response: any) => {
              console.log(response);

              _localStorageService.saveTokens(response);
              const newToken = _cookieService.get(_localStorageService.TOKEN);

              const newRequest = request.clone({
                setHeaders: {
                  Authorization: `Bearer ${newToken}`
                }
              });
              return next(newRequest);
            }),
            catchError((refreshError: any) => {
              console.log(refreshError);
              clearAll(_localStorageService, _cookieService);
              redirectHome("Error al obtener el token.", _alertService, _router);
              return throwError(() => refreshError);
            })
          )

        } else if(error.status === 403) {
          clearAll(_localStorageService, _cookieService);
          redirectHome("No se encuentra autenticado", _alertService, _router);
        }
      }
      return throwError(() => error);
    })
  );
};

const clearAll = (_localStorageService: any, _cookieService: any) => {
  _localStorageService.removeItem(_localStorageService.USER_ID);
  _localStorageService.removeItem(_localStorageService.USER_NAME);
  _localStorageService.removeItem(_localStorageService.USER_PHOTO);
  _cookieService.deleteAll();
}

const redirectHome = (message: string, _alertService: any, _router: any) => {

  _alertService.error("Error!", message);

  _router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
    _router.navigateByUrl("/home");
  });
}
