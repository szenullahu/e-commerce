import { Component, inject, Input } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../core/services/user/user.service';
import { UpdateEmailDto } from '../../../../core/models/user/update-email.dto';

@Component({
  selector: 'app-update-email',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-email.component.html'
})
export class UpdateEmailComponent {
  private userService = inject(UserService);
  private fb = inject(NonNullableFormBuilder);

  @Input() currentEmail!: string;

  emailForm = this.fb.group({
    newEmail: ['', [Validators.required, Validators.email]]
  });

  onUpdateEmail(): void {
    if (this.emailForm.valid) {
      const enteredEmail = this.emailForm.getRawValue().newEmail;

      if (enteredEmail === this.currentEmail) {
        alert('New email must be different from current email.');
        return;
      }

      const dto: UpdateEmailDto = {
        newEmail: enteredEmail
      };

      this.userService.updateEmail(dto).subscribe({
        next: () => {
          console.log('Email updated successfully');
          this.emailForm.reset();
        },
        error: (err) => console.error(err)
      });
    }
  }
}
