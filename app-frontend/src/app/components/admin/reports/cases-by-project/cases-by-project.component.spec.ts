import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesByProjectComponent } from './cases-by-project.component';

describe('CasesByProjectComponent', () => {
  let component: CasesByProjectComponent;
  let fixture: ComponentFixture<CasesByProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CasesByProjectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CasesByProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
