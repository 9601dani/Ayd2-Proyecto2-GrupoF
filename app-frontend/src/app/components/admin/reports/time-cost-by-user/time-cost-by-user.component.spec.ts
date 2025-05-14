import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeCostByUserComponent } from './time-cost-by-user.component';

describe('TimeCostByUserComponent', () => {
  let component: TimeCostByUserComponent;
  let fixture: ComponentFixture<TimeCostByUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimeCostByUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimeCostByUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
