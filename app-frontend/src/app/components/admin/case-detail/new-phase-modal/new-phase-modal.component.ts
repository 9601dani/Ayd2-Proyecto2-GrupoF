import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {ModalComponent} from "../../../commons/modal/modal.component";
import {CommonService} from '../../../../services/commons/common.service';
import {ProjectService} from '../../../../services/project/project.service';
import {AlertService} from '../../../../services/commons/alert.service';
import {Router} from '@angular/router';
import {UserService} from '../../../../services/user/user.service';
import {CommonModule, NgForOf, NgIf} from '@angular/common';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-new-phase-modal',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './new-phase-modal.component.html',
  styleUrl: './new-phase-modal.component.scss'
})
export class NewPhaseModalComponent implements OnInit {

  @Output() closed = new EventEmitter<any>();
  @Input() case: any;
  private _commonService: CommonService = inject(CommonService);
  private _projectService: ProjectService = inject(ProjectService);
  private _alertService: AlertService = inject(AlertService);
  private _userService: UserService = inject(UserService);
  private _router: Router = inject(Router);
  userControl = new FormControl(null, [Validators.required]);

  users: any[] = [];
  nextPhase: any;

  ngOnInit() {
    this.getNextPhase();
    this.getUsers();
  }

  getNextPhase() {
    this._projectService.getNextPhase(this.case.phaseId).subscribe({
      next: (response: any) => {
        if(response === null) {
          this.completeCase();
          this.closeModal();
          this.reloadPage();
        } else {
          this.nextPhase = response;
        }
      },
      error: (error: any) => {
        console.log(error.error.message);
      }
    })
  }

  getUsers() {
    this._userService.getUsersByRole(2).subscribe({
      next: (value) => (this.users = value),
      error: (err) => console.log(err),
    });
  }

  completeCase() {
    this._projectService.completeCase(this.case.id).subscribe({
      next: (response: any) => {
        this._alertService.success("Éxito!", "Se han completado todas las fases de este caso.");
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    })
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.closed.emit();
  }

  reloadPage() {
    const currentUrl = this._router.url;

    this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this._router.navigateByUrl(currentUrl);
    });
  }

  saveNextPhase() {
    if(this.userControl.invalid) {
      this._alertService.error("Error!", "Por favor seleccione un usuario.");
      return;
    }

    const userId = this.userControl.value;

    const data = {
      caseId: this.case.id,
      userId,
      nextPhaseId: this.nextPhase.id
    }

    this._projectService.saveNextPhase(data).subscribe({
      next: (response: any) => {
        console.log(response);
        this._alertService.success("Éxito!", "Nueva fase creada.");
        this.closeModal();
        this.reloadPage();
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    })
  }
}
