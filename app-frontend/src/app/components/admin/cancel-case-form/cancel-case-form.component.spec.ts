import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelCaseFormComponent } from './cancel-case-form.component';

describe('CancelCaseFormComponent', () => {
  let component: CancelCaseFormComponent;
  let fixture: ComponentFixture<CancelCaseFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CancelCaseFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CancelCaseFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
