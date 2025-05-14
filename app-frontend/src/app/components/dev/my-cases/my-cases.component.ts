import { Component } from '@angular/core';
import { TemplateComponent } from '../../commons/template/template.component';
import { ProjectService } from '../../../services/project/project.service';
import { LocalStorageService } from '../../../services/commons/local-storage.service';
import { AlertService } from '../../../services/commons/alert.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-cases',
  standalone: true,
  imports: [TemplateComponent, CommonModule],
  templateUrl: './my-cases.component.html',
  styleUrl: './my-cases.component.scss'
})
export class MyCasesComponent {
  cases: any[] = [];

  constructor(private _projectService:ProjectService, private _localStorage:LocalStorageService, private _alertService:AlertService, private _router:Router){}

  ngOnInit() {
    const username = this._localStorage.getItem(this._localStorage.USER_NAME);
  
    this._projectService.getMyCases(username).subscribe({
      next: (response) => {
        if (response.length === 0) {
          this._alertService.success('Sin casos', 'No posees casos activos');
        } else {
          this.cases = response;
          console.log(this.cases)
        }
      },
      error: (err) => {
        this._alertService.warning(
          'No posees casos activos',
          err?.error?.message || 'No hay casos activos para ti'
        );
      },
    });
  }

  viewCase(caseItem: any) {
      this._router.navigate([`/case/${caseItem.caseId}`]);

  }
  

}
