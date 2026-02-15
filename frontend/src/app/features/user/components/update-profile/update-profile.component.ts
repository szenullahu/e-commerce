import { Component, inject, Input, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from '../../../../core/services/user/user.service';
import {User} from '../../../../core/models/user/user.model';
import {UpdateProfileInfoDto} from '../../../../core/models/user/update-profile-info.dto';

@Component({
  selector: 'app-update-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-profile.component.html'
})
export class UpdateProfileComponent implements OnInit {
  private userService = inject(UserService);
  private fb = inject(NonNullableFormBuilder);

  @Input() userData!: User;

  profileForm = this.fb.group({
    firstname: ['', [Validators.required]],
    surname: ['', [Validators.required]],
  });

  ngOnInit(): void {
    if (this.userData) {
      this.profileForm.patchValue({
        firstname: this.userData.firstname,
        surname: this.userData.surname,
      });
    }
  }

  onUpdateProfile(): void {
    if (this.profileForm.valid) {
      const dto: UpdateProfileInfoDto = this.profileForm.getRawValue() as UpdateProfileInfoDto;

      this.userService.updateProfileInfo(dto).subscribe({
        next: (updatedUser) => {
          console.log('Profile updated successfully', updatedUser);
        },
        error: (err) => {
          console.error('Profile update failed', err);
        }
      });
    }
  }
}
