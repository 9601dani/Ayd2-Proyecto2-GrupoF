import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeCostByDateComponent } from './time-cost-by-date.component';

describe('TimeCostByDateComponent', () => {
  let component: TimeCostByDateComponent;
  let fixture: ComponentFixture<TimeCostByDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimeCostByDateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimeCostByDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
