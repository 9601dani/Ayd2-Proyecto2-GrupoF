import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsReportComponent } from './projects-report.component';

describe('ProjectsReportComponent', () => {
  let component: ProjectsReportComponent;
  let fixture: ComponentFixture<ProjectsReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectsReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
