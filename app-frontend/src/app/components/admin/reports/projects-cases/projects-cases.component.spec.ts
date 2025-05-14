import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsCasesComponent } from './projects-cases.component';

describe('ProjectsCasesComponent', () => {
  let component: ProjectsCasesComponent;
  let fixture: ComponentFixture<ProjectsCasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectsCasesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectsCasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
