import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {ModalComponent} from "../../../commons/modal/modal.component";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonService} from '../../../../services/commons/common.service';
import {AlertService} from '../../../../services/commons/alert.service';
import {CommonModule} from '@angular/common';
import {ProjectService} from '../../../../services/project/project.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-complete-phase-modal',
  standalone: true,
    imports: [
      ModalComponent,
      ReactiveFormsModule,
      CommonModule
    ],
  templateUrl: './complete-phase-modal.component.html',
  styleUrl: './complete-phase-modal.component.scss'
})
export class CompletePhaseModalComponent implements OnInit {

  @Output() closed = new EventEmitter<void>();
  @Input() case: any;

  private _commonService: CommonService = inject(CommonService);
  private _alertService: AlertService = inject(AlertService);
  private _projectService: ProjectService = inject(ProjectService);
  private _router: Router = inject(Router);
  timeSpentControl = new FormControl(0, [Validators.required, Validators.min(0)])

  ngOnInit() {
    console.log(this.case);
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.closed.emit();
  }

  updatePhase() {
    if(this.timeSpentControl.invalid) {
      this._alertService.error("Error!", "Por favor ingresa un valor válido.");
      return;
    }

    const timeSpent = this.timeSpentControl.value;
    console.log(timeSpent);
    const data = {
      id: this.case.historyId,
      isCompleted: true,
      timeSpent
    }

    this._projectService.updateCasePhase(data).subscribe({
      next: (response: any) => {
        this._alertService.success("Éxito!", "Se ha completado la fase exitosamente.");
        this.closeModal();
        this.reloadPage();
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    })
  }

  reloadPage() {
    const currentUrl = this._router.url;

    this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this._router.navigateByUrl(currentUrl);
    });
  }
}
