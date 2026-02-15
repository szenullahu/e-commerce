import {Component, inject} from '@angular/core';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {UpdatePasswordDto} from '../../../../core/models/user/update-password.dto';
import {UserService} from '../../../../core/services/user/user.service';

export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const newPassword = control.get('newPassword');
  const confirmPassword = control.get('confirmPassword');

  if (!newPassword || !confirmPassword) {
    return null;
  }

  return newPassword.value === confirmPassword.value ? null : { passwordMismatch: true };
};

@Component({
  selector: 'app-update-password',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './update-password.component.html',
  styleUrl: './update-password.component.scss',
})
export class UpdatePasswordComponent {
  private userService = inject(UserService);
  private fb = inject(NonNullableFormBuilder);

  passwordForm = this.fb.group({
    oldPassword: ['', [Validators.required]],
    newPassword: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', [Validators.required]]
  }, { validators: passwordMatchValidator });

  message: string | null = null;
  error: string | null = null;

  onUpdatePassword(): void {
    this.message = null;
    this.error = null;

    if (this.passwordForm.valid) {
      const { oldPassword, newPassword } = this.passwordForm.getRawValue();
      const dto: UpdatePasswordDto = { oldPassword, newPassword };

      this.userService.updatePassword(dto).subscribe({
        next: () => {
          this.message = 'Password changed successfully.';
          this.passwordForm.reset();
        },
        error: (err) => {
          this.error = 'Could not change password. Please check your old password.';
          console.error(err);
        }
      });
    }
  }

}
