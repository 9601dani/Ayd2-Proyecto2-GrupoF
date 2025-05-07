import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisabledProjectComponent } from './disabled-project.component';

describe('DisabledProjectComponent', () => {
  let component: DisabledProjectComponent;
  let fixture: ComponentFixture<DisabledProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisabledProjectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DisabledProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
