import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasePhaseFormComponent } from './case-phase-form.component';

describe('CasePhaseFormComponent', () => {
  let component: CasePhaseFormComponent;
  let fixture: ComponentFixture<CasePhaseFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CasePhaseFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CasePhaseFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
