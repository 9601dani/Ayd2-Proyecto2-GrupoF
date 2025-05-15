import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface UserResponseWithName {
  id: number;
  username: string;
  email: string;
  salaryPerHour: number;
  isEnabled: boolean;
  firstName: string;
  lastName: string;
}


@Component({
  selector: 'app-users-report',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule],
  templateUrl: './users-report.component.html',
  styleUrl: './users-report.component.scss'
})
export class UsersReportComponent implements OnInit {
  allUsers: UserResponseWithName[] = [];
  filteredUsers: UserResponseWithName[] = [];

  search: string = '';
  minSalary: number | null = null;
  stateFilter: boolean | null = null;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport6().subscribe({
      next: (data) => {
        this.allUsers = data;
        this.applyFilters();
      },
      error: (err) => console.error(err),
    });
  }

  applyFilters(): void {
    this.filteredUsers = this.allUsers.filter((user) => {
      const matchState = this.stateFilter === null || user.isEnabled === this.stateFilter;
      const matchSalary = this.minSalary === null || user.salaryPerHour >= this.minSalary;
      const matchSearch =
        this.search.trim() === '' ||
        user.username.toLowerCase().includes(this.search.toLowerCase()) ||
        `${user.firstName} ${user.lastName}`.toLowerCase().includes(this.search.toLowerCase());

      return matchState && matchSalary && matchSearch;
    });
  }
}
