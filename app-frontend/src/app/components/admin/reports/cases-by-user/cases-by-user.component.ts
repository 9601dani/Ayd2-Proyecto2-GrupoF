import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { UserService } from '../../../../services/user/user.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface CaseUserReportDto {
  caseId: number;
  caseName: string;
  description: string;
  caseTypeId: number;
  caseTypeName: string;
  createdAt: string;
  limitDate: string;
  userId: number;
}


@Component({
  selector: 'app-cases-by-user',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './cases-by-user.component.html',
  styleUrl: './cases-by-user.component.scss'
})
export class CasesByUserComponent implements OnInit {
  users: any[] = [];
  selectedUserId: number | undefined;
  report: CaseUserReportDto[] = [];

  constructor(
    private _reportService: ReportService,
    private _userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadReport(); // Load all initially
  }

  loadUsers(): void {
    this._userService.getUsersByRole(2).subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error(err)
    });
  }

  loadReport(): void {
    this._reportService.getReport13(this.selectedUserId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }
}
