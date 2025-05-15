import {Component, inject, OnInit} from '@angular/core';
import {TemplateComponent} from '../../commons/template/template.component';
import {ActivatedRoute, Router} from '@angular/router';
import {CommentsComponent} from './comments/comments.component';
import {AlertService} from '../../../services/commons/alert.service';
import {MatDivider} from '@angular/material/divider';
import {ProjectService} from '../../../services/project/project.service';
import { DatePipe } from '@angular/common';
import {UserService} from '../../../services/user/user.service';
import {LocalStorageService} from '../../../services/commons/local-storage.service';
import {CompletePhaseModalComponent} from './complete-phase-modal/complete-phase-modal.component';
import {NewPhaseModalComponent} from './new-phase-modal/new-phase-modal.component';
import {CommonService} from '../../../services/commons/common.service';

@Component({
  selector: 'app-case-detail',
  standalone: true,
  imports: [
    TemplateComponent,
    CommentsComponent,
    DatePipe,
    CompletePhaseModalComponent,
    NewPhaseModalComponent,
  ],
  templateUrl: './case-detail.component.html',
  styleUrl: './case-detail.component.scss'
})
export class CaseDetailComponent implements OnInit {

  id: any;
  userRole: number = 0;
  userId: number = 0;
  modalActive: 'complete' | 'new' | null = null;
  private _activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private _projectService: ProjectService = inject(ProjectService);
  private _userService: UserService = inject(UserService);
  private _router: Router = inject(Router);
  private _alertService: AlertService = inject(AlertService);
  private _localStorageService: LocalStorageService = inject(LocalStorageService);
  private _commonService: CommonService = inject(CommonService);
  case: any;

  ngOnInit() {
    this.userId = this._localStorageService.getItem(this._localStorageService.USER_ID);
    this._activatedRoute.params.subscribe(data => this.id = data["id"]);

    if(isNaN(this.id)) {
      this._alertService.error("Error!", "No se encontró la ruta.");
      this._router.navigate(["/home"]);
      return;
    }

    this.getCaseDetails();
    this.getUserDetails();
  }

  getCaseDetails() {
    this._projectService.getCaseDetails(this.id).subscribe({
      next: (response: any) => {
        console.log(response);
        this.case = response;
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    })
  }

  getUserDetails() {
    this._userService.getUserById(this.userId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.userRole = response.role;
      }
    })
  }

  markAsCompleted() {
    this.modalActive = 'complete';
    this._commonService.emitActiveModal(true);
  }

  markAsAccepted() {
    this.modalActive = 'new';
    this._commonService.emitActiveModal(true);
  }

  markAsRejected() {
    this._alertService.yesNo("¿Está seguro?", "El desarrollador deberá volver a trabajar en esta fase.", (result: any) => {
      if(result.isConfirmed) {
        const data = {
          id: this.case.historyId,
          timeSpent: null,
          isCompleted: false
        }

        this._projectService.updateCasePhase(data).subscribe({
          next: (response: any) => {
            console.log(response);
            this._alertService.success("Éxito!", "Se ha rechazado la fase exitosamente.");
            this.reloadPage();
          },
          error: (error: any) => {
            this._alertService.error("Error!", error.error.message);
          }
        })
      }
    })
  }

  reloadPage() {
    const currentUrl = this._router.url;

    this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this._router.navigateByUrl(currentUrl);
    });
  }

  onModalClosed() {
    this.modalActive = null;
  }

}
