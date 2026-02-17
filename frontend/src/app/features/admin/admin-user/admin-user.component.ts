import {Component, inject, OnInit, signal} from '@angular/core';
import {AdminUserService} from '../../../core/services/admin/admin-user.service';
import {User} from '../../../core/models/user/user.model';
import {NonNullableFormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {ProfilePictureOption} from '../../../core/models/user/profile-picture.enum';
import {RegisterAdmin} from '../../../core/models/admin/register-admin.model';

@Component({
  selector: 'app-admin-user',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './admin-user.component.html',
  styleUrl: './admin-user.component.scss',
})
export class AdminUserComponent implements OnInit {
  private adminUserService= inject(AdminUserService);
  private fb = inject(NonNullableFormBuilder);

  profileOptions = Object.values(ProfilePictureOption);

  users = signal<User[]>([]);
  loading = signal(true);

  showModal = signal(false);


  adminForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    firstname: ['', Validators.required],
    surname: ['', Validators.required],
    profilePicture: [null as ProfilePictureOption | null, Validators.required]
  })

  ngOnInit() {
    this.loadUsers()
  }

  getAvatarSrc(option: string): string {
    return `assets/avatars/${option.toLowerCase()}.png`;
  }

  selectAvatar(option: ProfilePictureOption) {
    this.adminForm.patchValue({ profilePicture: option });
  }

  loadUsers() {
    this.loading.set(true);
    this.adminUserService.getUsers().subscribe({
      next: (data) => {
        this.users.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.log(error);
        this.loading.set(false);
      }
    })
  }

  protected onDeleteUser(user: User) {
    if (!confirm(`Are you sure you want to delete user "${user.username}"?`)) {
      return;
    }
    this.adminUserService.deleteUser(user.id).subscribe({
      next: (response) => {
        this.users.update(current => current.filter(u => u.id !== user.id));
        alert(response.message);
      },
      error: (err) => alert('Could not delete user.')
    });

  }

  openModal() {
    this.showModal.set(true);
  }

  closeModal() {
    this.showModal.set(false);
    this.adminForm.reset();
  }

  onSubmitAdmin() {
    if (this.adminForm.valid) {
      const dto = this.adminForm.getRawValue() as RegisterAdmin;

      this.adminUserService.createAdmin(dto).subscribe({
        next: (response) => {
          alert(response.message);
          this.closeModal();
          this.loadUsers();
        },
        error: (err) => alert('Failed to create admin.')
      });
    }
  }
}
