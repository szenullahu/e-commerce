import {Component, inject, OnInit, signal} from '@angular/core';
import {UserService} from '../../core/services/user/user.service';
import {User} from '../../core/models/user/user.model';
import {UpdateEmailComponent} from './components/update-email/update-email.component';
import {UpdatePasswordComponent} from './components/update-password/update-password.component';
import {UpdateProfileComponent} from './components/update-profile/update-profile.component';
import {UpdateProfilePictureComponent} from './components/update-profile-picture/update-profile-picture.component';
import {ProfilePictureOption} from '../../core/models/user/profile-picture.enum';

type Tab = 'OVERVIEW' | 'SETTINGS' | 'SECURITY';

@Component({
  selector: 'app-user',
  imports: [
    UpdateProfileComponent,
    UpdateEmailComponent,
    UpdateProfilePictureComponent,
    UpdatePasswordComponent
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent implements OnInit{
  private userService = inject(UserService);


  user = signal<User | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);
  activeTab = signal<'OVERVIEW' | 'SETTINGS' | 'SECURITY'>('OVERVIEW');  showAvatarModal = signal(false);

  ngOnInit(): void {
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.user.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Something went wrong.');
        this.loading.set(false);
      },
    });
  }

  setActiveTab(tab: Tab) {
    this.activeTab.set(tab);
  }

  toggleAvatarModal() {
    this.showAvatarModal.update(v => !v);
  }

  avatarSrc(option: ProfilePictureOption | string | null | undefined): string {
    return `assets/avatars/${option?.toLowerCase() || 'gojo'}.png`;
  }
}
