import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeCostCaseTypeComponent } from './time-cost-case-type.component';

describe('TimeCostCaseTypeComponent', () => {
  let component: TimeCostCaseTypeComponent;
  let fixture: ComponentFixture<TimeCostCaseTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimeCostCaseTypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimeCostCaseTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
