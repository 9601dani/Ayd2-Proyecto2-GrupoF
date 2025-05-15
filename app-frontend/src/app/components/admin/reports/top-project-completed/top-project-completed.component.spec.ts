import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopProjectCompletedComponent } from './top-project-completed.component';

describe('TopProjectCompletedComponent', () => {
  let component: TopProjectCompletedComponent;
  let fixture: ComponentFixture<TopProjectCompletedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopProjectCompletedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopProjectCompletedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
