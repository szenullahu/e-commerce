import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {UserService} from '../../../../core/services/user/user.service';
import {ProfilePictureOption} from '../../../../core/models/user/profile-picture.enum';

@Component({
  selector: 'app-update-profile-picture',
  imports: [],
  templateUrl: './update-profile-picture.component.html',
  styleUrl: './update-profile-picture.component.scss',
})
export class UpdateProfilePictureComponent implements OnInit {
  private userService = inject(UserService);

  @Input() currentPicture: ProfilePictureOption | null = null;
  @Output() close = new EventEmitter<void>();

  profileOptions = Object.values(ProfilePictureOption);
  pendingSelection: ProfilePictureOption | null = null;
  isSaving = false;

  ngOnInit(): void {
    this.pendingSelection = this.currentPicture;
  }

  selectAvatar(option: ProfilePictureOption) {
    this.pendingSelection = option;
  }

  save() {
    if (this.pendingSelection) {
      this.isSaving = true;
      this.userService.updateProfilePicture({ profilePicture: this.pendingSelection }).subscribe({
        next: () => {
          this.isSaving = false;
          this.close.emit();
        },
        error: () => {
          this.isSaving = false;
        }
      });
    }
  }

  cancel() {
    this.close.emit();
  }

  avatarSrc(option: ProfilePictureOption | string | null | undefined): string {
    return `assets/avatars/${option?.toLowerCase() || 'geto'}.png`;
  }
}
