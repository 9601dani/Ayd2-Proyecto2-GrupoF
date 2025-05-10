import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseTypeFormComponent } from './case-type-form.component';

describe('CaseTypeFormComponent', () => {
  let component: CaseTypeFormComponent;
  let fixture: ComponentFixture<CaseTypeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaseTypeFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CaseTypeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
