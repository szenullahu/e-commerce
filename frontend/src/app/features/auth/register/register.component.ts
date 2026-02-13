import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../../core/services/auth/auth.service';
import {ProfilePictureOption} from '../../../core/models/user/profile-picture.enum';
import {RegisterRequest} from '../../../core/models/auth/register-request.model';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  profilePictureOptions = Object.values(ProfilePictureOption);

  form = this.fb.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    firstname: ['', Validators.required],
    surname: ['', Validators.required],
    profilePicture: [null as ProfilePictureOption | null, Validators.required],
  })

  isLoading = false;
  errorMessage= '';

  onSubmit() {
    if (this.form.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const request: RegisterRequest = this.form.value as RegisterRequest;

      this.authService.register(request).subscribe({
        next: (response)=>  {
          console.log("Register", response);
          this.isLoading = false;
          this.router.navigate(['/']);
        },
        error: (error) => {
          this.isLoading = false;
          console.error(error);
          this.errorMessage = 'Registration failed.';
        }
      });
    } else {
      this.form.markAllAsTouched();
    }
  }

}
