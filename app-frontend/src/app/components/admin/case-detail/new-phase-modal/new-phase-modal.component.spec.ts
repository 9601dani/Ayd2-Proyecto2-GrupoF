import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPhaseModalComponent } from './new-phase-modal.component';

describe('NewPhaseModalComponent', () => {
  let component: NewPhaseModalComponent;
  let fixture: ComponentFixture<NewPhaseModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewPhaseModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewPhaseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
