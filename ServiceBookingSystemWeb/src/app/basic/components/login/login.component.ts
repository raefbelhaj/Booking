import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Router } from '@angular/router';
import { UserStorageService } from '../../services/storage/user-storage.service';

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
    this.authService.login(this.validateForm.get('userName')!.value, this.validateForm.get('password')!.value)
      .subscribe(
        res => {
          console.log(res);
          // Assuming the authService sets the appropriate flag in UserStorageService
          if (UserStorageService.isCLientLoggedIn()) {
            this.router.navigateByUrl('client/dashboard');
          } else if (UserStorageService.isCompanyLoggedIn()) {
            this.router.navigateByUrl('company/dashboard');
          } else {
            // Redirect to a default page if neither client nor company logged in
            this.router.navigateByUrl('/home');
          }
        },
        error => {
          this.notification.error(
            'ERROR',
            'Bad credentials',
            { nzDuration: 5000 }
          );
      } 
      );
  }
}