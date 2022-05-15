import { Module } from '@nestjs/common';
import { PassportModule } from '@nestjs/passport';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from './user/user.entity';
import { UsersController } from './user/users.controller';
import { UsersService } from './user/users.service';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'sqlite',
      database: 'database.sqlite',
      entities: [User],
      migrations: ['dist/migrations/*.js'],
      migrationsRun: true,
    }),
    TypeOrmModule.forFeature([User]),
    PassportModule,
  ],
  controllers: [UsersController],
  providers: [UsersService],
})
export class AppModule {}
