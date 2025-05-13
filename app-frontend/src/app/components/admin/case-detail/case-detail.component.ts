import {Component, inject, OnInit} from '@angular/core';
import {TemplateComponent} from '../../commons/template/template.component';
import {ActivatedRoute, Router} from '@angular/router';
import {CommentsComponent} from './comments/comments.component';
import {AlertService} from '../../../services/commons/alert.service';
import {MatDivider} from '@angular/material/divider';
import {ProjectService} from '../../../services/project/project.service';

@Component({
  selector: 'app-case-detail',
  standalone: true,
  imports: [
    TemplateComponent,
    CommentsComponent,
    MatDivider
  ],
  templateUrl: './case-detail.component.html',
  styleUrl: './case-detail.component.scss'
})
export class CaseDetailComponent implements OnInit {

  id: any;
  private _activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private _projectService: ProjectService = inject(ProjectService);
  private _router: Router = inject(Router);
  private _alertService: AlertService = inject(AlertService);
  case: any;

  ngOnInit() {
    this._activatedRoute.params.subscribe(data => this.id = data["id"]);

    if(isNaN(this.id)) {
      this._alertService.error("Error!", "No se encontró la ruta.");
      this._router.navigate(["/home"]);
      return;
    }

    this.getCaseDetails();
  }

  getCaseDetails() {
    this._projectService.getCaseById(this.id).subscribe({
      next: (response: any) => {
        console.log(response);
        this.case = response;
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    })
  }
}
