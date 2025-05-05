import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonService} from '../../../services/commons/common.service';
import {Subscription} from 'rxjs';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss'
})
export class ModalComponent implements OnInit, OnDestroy {

  private subscription!: Subscription;
  isActive: boolean = false;

  constructor(private _commonService: CommonService) {
  }

  ngOnInit(): void {
    this.subscribeToActiveModal();
  }

  subscribeToActiveModal() {
    this.subscription = this._commonService.getActiveModal().subscribe({
      next: (value: boolean) => {
        this.isActive = value;
      },
      error: (error) => {
        console.error(error);
      }
    })
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
  }

}
