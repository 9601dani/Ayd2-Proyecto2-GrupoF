import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletePhaseModalComponent } from './complete-phase-modal.component';

describe('CompletePhaseModalComponent', () => {
  let component: CompletePhaseModalComponent;
  let fixture: ComponentFixture<CompletePhaseModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletePhaseModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompletePhaseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
