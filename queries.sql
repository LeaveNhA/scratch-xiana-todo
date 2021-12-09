-- Local Variables:
-- eval: (setq-local usr '(user    :default "postgres"))
-- eval: (setq-local db '(database :default "hello"))
-- eval: (setq-local srv '(server  :default "localhost"))
-- eval: (setq-local prt '(port    :default 5433))
-- eval: (setq-local sql-postgres-login-params `(,usr ,db ,srv ,prt))
-- eval: (sql-postgres)
-- End:

\d

\d todo

\d migrations

\?

select * from todo;

