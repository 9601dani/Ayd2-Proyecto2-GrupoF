import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopProjectCancelledComponent } from './top-project-cancelled.component';

describe('TopProjectCancelledComponent', () => {
  let component: TopProjectCancelledComponent;
  let fixture: ComponentFixture<TopProjectCancelledComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopProjectCancelledComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopProjectCancelledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
