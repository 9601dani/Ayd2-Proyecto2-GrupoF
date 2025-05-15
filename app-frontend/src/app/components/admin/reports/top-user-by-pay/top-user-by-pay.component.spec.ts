import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopUserByPayComponent } from './top-user-by-pay.component';

describe('TopUserByPayComponent', () => {
  let component: TopUserByPayComponent;
  let fixture: ComponentFixture<TopUserByPayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopUserByPayComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopUserByPayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
