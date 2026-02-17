import {ProfilePictureOption} from '../user/profile-picture.enum';

export interface RegisterAdmin {
  username: string;
  email: string;
  password: string;
  firstname: string;
  surname: string;
  profilePicture: ProfilePictureOption;
}
