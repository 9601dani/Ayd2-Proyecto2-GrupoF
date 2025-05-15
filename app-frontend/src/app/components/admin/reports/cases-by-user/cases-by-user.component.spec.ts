import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesByUserComponent } from './cases-by-user.component';

describe('CasesByUserComponent', () => {
  let component: CasesByUserComponent;
  let fixture: ComponentFixture<CasesByUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CasesByUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CasesByUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
