import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeCostByProjectComponent } from './time-cost-by-project.component';

describe('TimeCostByProjectComponent', () => {
  let component: TimeCostByProjectComponent;
  let fixture: ComponentFixture<TimeCostByProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimeCostByProjectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimeCostByProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
