import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopUserByCasesComponent } from './top-user-by-cases.component';

describe('TopUserByCasesComponent', () => {
  let component: TopUserByCasesComponent;
  let fixture: ComponentFixture<TopUserByCasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopUserByCasesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopUserByCasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
