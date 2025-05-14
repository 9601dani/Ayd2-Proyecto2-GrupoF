import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { UserService } from '../../../../services/user/user.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface Report3Dto {
  id: number;
  username: string;
  salaryPerHour: number;
  totalHours: number;
  totalSalary: number;
}

@Component({
  selector: 'app-time-cost-by-user',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './time-cost-by-user.component.html',
  styleUrl: './time-cost-by-user.component.scss'
})
export class TimeCostByUserComponent implements OnInit {
  report: Report3Dto[] = [];
  users: any[] = [];
  userId: number | undefined;

  constructor(private _reportService: ReportService, private _userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadReport();
  }

  loadUsers(): void {
    this._userService.getUsersByRole(2).subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error(err)
    });
  }

  loadReport(): void {
    this._reportService.getReport3(this.userId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }
}
