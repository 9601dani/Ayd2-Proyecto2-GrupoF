import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  isLogin = true;

  ngOnInit(){

  }

  constructor(private _router: Router){

  }

  

onSubmit() {
  if (this.isLogin) {
    this._router.navigate(['home']);
  } else {
    this.isLogin= true
  }
}

}
