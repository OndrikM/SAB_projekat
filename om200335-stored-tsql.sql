
CREATE PROCEDURE spGrantRequest
	@korisnickoIme varchar(100)
AS
BEGIN
	 declare @registracija varchar(100)
        
     select @registracija=Registracija from ZAHTEV_KURIR where KorisnickoIme=@korisnickoIme
   
     delete from ZAHTEV_KURIR where KorisnickoIme=@korisnickoIme

     delete from ZAHTEV_KURIR where Registracija=@registracija
     
	 insert into KURIR(KorisnickoIme,Registracija,BrojPaketa,Profit,Status) values (@korisnickoIme,@registracija,0,0,0)

END
GO
