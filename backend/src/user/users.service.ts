import { BadRequestException, Injectable, UnauthorizedException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './user.entity';
import { genSalt, hash } from 'bcrypt';

@Injectable()
export class UsersService {
  private static SALT_ROUNDS = 10;

  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
  ) {}

  async register(credentials: Credentials): Promise<User> {
    const user = await this.findByEmail(credentials.email);
    if (user === undefined) {
      const password_salt = await genSalt(UsersService.SALT_ROUNDS);
      const password_hash = await hash(credentials.password, password_salt);
      return await this.usersRepository.save({
        email: credentials.email.toLowerCase(),
        password: password_hash,
        salt: password_salt,
      });
    } else {
      throw new BadRequestException('Email is already used');
    }
  }

  async login(credentials: Credentials): Promise<User> {
    const user = await this.findByEmail(credentials.email);
    if (user !== undefined) {
      const password_hash = await hash(credentials.password, user.salt);
      if (password_hash === user.password) {
        return Promise.resolve(user);
      }
    }
    throw new UnauthorizedException();
  }

  findAll(): Promise<User[]> {
    return this.usersRepository.find();
  }

  async findByEmail(email: string): Promise<User | undefined> {
    return this.usersRepository.findOne({
      where: [{ email: email.toLowerCase() }],
    });
  }
}

type Credentials = { email: string; password: string };
