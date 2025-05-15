import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesByTypeComponent } from './cases-by-type.component';

describe('CasesByTypeComponent', () => {
  let component: CasesByTypeComponent;
  let fixture: ComponentFixture<CasesByTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CasesByTypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CasesByTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
