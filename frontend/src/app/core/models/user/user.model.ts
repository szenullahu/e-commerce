import {Role} from './role.enum';
import {ProfilePictureOption} from './profile-picture.enum';

export interface User {
  id: string;
  username: string;
  email: string;
  firstname: string;
  surname: string;
  profilePicture: ProfilePictureOption | null;
  role: Role;
}
