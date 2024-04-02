import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  validateForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notification: NzNotificationService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });
  }

  submitForm() {
    if (this.validateForm.valid) {
      const username = this.validateForm.get('userName')!.value;
      const password = this.validateForm.get('password')!.value;

      this.authService.login(username, password)
        .subscribe(
          res => {
            console.log(res);
            // Rediriger vers une autre page après la connexion réussie
            this.router.navigate(['/dashboard']);
          },
          error => {
            this.notification.error(
              'ERROR',
              'Bad credentials',
              { nzDuration: 5000 }
            );
          }
        );
    } else {
      console.error('Form is invalid');
    }
  }
}
