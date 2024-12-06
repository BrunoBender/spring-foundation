CREATE TABLE `user_permission` (
  `id` INT(10) AUTO_INCREMENT PRIMARY KEY,
  `id_user` INT(10) NOT NULL,
  `id_permission` INT(10) NOT NULL,
  CONSTRAINT `id_user_fk` FOREIGN KEY (`id_user`) references `users` (`id`),
  CONSTRAINT `id_permission_fk` FOREIGN KEY (`id_permission`) references `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
