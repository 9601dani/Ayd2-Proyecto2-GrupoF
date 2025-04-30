import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ModalComponent} from './components/commons/modal/modal.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ModalComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'app-frontend';
}
