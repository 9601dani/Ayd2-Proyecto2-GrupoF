import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsModuleComponent } from './settings-module.component';

describe('SettingsModuleComponent', () => {
  let component: SettingsModuleComponent;
  let fixture: ComponentFixture<SettingsModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsModuleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
