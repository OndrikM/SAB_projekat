
CREATE TABLE [GRAD]
( 
	[Naziv]              varchar(100)  NOT NULL ,
	[PostanskiBroj]      varchar(100)  NOT NULL ,
	[IdG]                integer  NOT NULL  IDENTITY 
)
go

ALTER TABLE [GRAD]
	ADD PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

ALTER TABLE [GRAD]
	ADD UNIQUE ([Naziv]  ASC)
go

ALTER TABLE [GRAD]
	ADD UNIQUE ([PostanskiBroj]  ASC)
go

CREATE TABLE [KORISNIK]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[BrojPoslatihPaketa] integer  NOT NULL 
		CHECK  ( BrojPoslatihPaketa >= 0 ),
	[Admin]              integer  NOT NULL 
		CHECK  ( Admin BETWEEN 0 AND 1 )
)
go

ALTER TABLE [KORISNIK]
	ADD PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

CREATE TABLE [KURIR]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Registracija]       varchar(100)  NOT NULL ,
	[BrojPaketa]         integer  NOT NULL 
		CHECK  ( BrojPaketa BETWEEN 0 AND 3 ),
	[Profit]             decimal(10,3)  NOT NULL ,
	[Status]             integer  NOT NULL 
		CHECK  ( Status BETWEEN 0 AND 1 )
)
go

ALTER TABLE [KURIR]
	ADD PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

CREATE TABLE [OPSTINA]
( 
	[IdO]                integer  NOT NULL  IDENTITY ,
	[IdG]                integer  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL 
)
go

ALTER TABLE [OPSTINA]
	ADD PRIMARY KEY  CLUSTERED ([IdO] ASC)
go

ALTER TABLE [OPSTINA]
	ADD UNIQUE ([Naziv]  ASC)
go

CREATE TABLE [PAKET]
( 
	[Kurir]              varchar(100)  NULL ,
	[IdP]                integer  NOT NULL  IDENTITY ,
	[Status]             integer  NOT NULL ,
	[Cena]               decimal(10,3)  NULL 
		CHECK  ( Cena >= 0 ),
	[DatumPrihvatanja]   datetime  NULL 
)
go

ALTER TABLE [PAKET]
	ADD PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

CREATE TABLE [PONUDA]
( 
	[IdPon]              integer  NOT NULL  IDENTITY ,
	[Procenat]           decimal(10,3)  NOT NULL ,
	[Posiljalac]         varchar(100)  NULL ,
	[Kurir]              varchar(100)  NOT NULL ,
	[IdP]                integer  NULL 
)
go

ALTER TABLE [PONUDA]
	ADD PRIMARY KEY  CLUSTERED ([IdPon] ASC)
go

CREATE TABLE [PREVOZ]
( 
	[IdV]                integer  NOT NULL ,
	[IdP]                integer  NOT NULL 
)
go

ALTER TABLE [PREVOZ]
	ADD PRIMARY KEY  CLUSTERED ([IdV] ASC,[IdP] ASC)
go

CREATE TABLE [VOZILO]
( 
	[Registracija]       varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL 
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NOT NULL 
)
go

ALTER TABLE [VOZILO]
	ADD PRIMARY KEY  CLUSTERED ([Registracija] ASC)
go

CREATE TABLE [VOZNJA]
( 
	[Kurir]              varchar(100)  NOT NULL ,
	[Profit]             decimal(10,3)  NOT NULL ,
	[IdV]                integer  NOT NULL  IDENTITY 
)
go

ALTER TABLE [VOZNJA]
	ADD PRIMARY KEY  CLUSTERED ([IdV] ASC)
go

CREATE TABLE [ZAHTEV_KURIR]
( 
	[Registracija]       varchar(100)  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

ALTER TABLE [ZAHTEV_KURIR]
	ADD PRIMARY KEY  CLUSTERED ([Registracija] ASC,[KorisnickoIme] ASC)
go

CREATE TABLE [ZAHTEV_PREVOZ]
( 
	[Posiljalac]         varchar(100)  NOT NULL ,
	[IdP]                integer  NOT NULL ,
	[OpstinaOd]          integer  NOT NULL ,
	[OpstinaDo]          integer  NOT NULL ,
	[Tezina]             decimal(10,3)  NOT NULL 
		CHECK  ( Tezina >= 0 ),
	[Tip]                integer  NOT NULL 
		CHECK  ( Tip BETWEEN 0 AND 2 )
)
go

ALTER TABLE [ZAHTEV_PREVOZ]
	ADD PRIMARY KEY  CLUSTERED ([Posiljalac] ASC,[IdP] ASC)
go


ALTER TABLE [KURIR]
	ADD  FOREIGN KEY ([KorisnickoIme]) REFERENCES [KORISNIK]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [KURIR]
	ADD  FOREIGN KEY ([Registracija]) REFERENCES [VOZILO]([Registracija])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [OPSTINA]
	ADD  FOREIGN KEY ([IdG]) REFERENCES [GRAD]([IdG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [PAKET]
	ADD  FOREIGN KEY ([Kurir]) REFERENCES [KURIR]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [PONUDA]
	ADD  FOREIGN KEY ([Posiljalac],[IdP]) REFERENCES [ZAHTEV_PREVOZ]([Posiljalac],[IdP])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [PONUDA]
	ADD  FOREIGN KEY ([Kurir]) REFERENCES [KURIR]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [PREVOZ]
	ADD  FOREIGN KEY ([IdV]) REFERENCES [VOZNJA]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [PREVOZ]
	ADD  FOREIGN KEY ([IdP]) REFERENCES [PAKET]([IdP])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [VOZNJA]
	ADD  FOREIGN KEY ([Kurir]) REFERENCES [KURIR]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZAHTEV_KURIR]
	ADD  FOREIGN KEY ([Registracija]) REFERENCES [VOZILO]([Registracija])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZAHTEV_KURIR]
	ADD  FOREIGN KEY ([KorisnickoIme]) REFERENCES [KORISNIK]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZAHTEV_PREVOZ]
	ADD  FOREIGN KEY ([Posiljalac]) REFERENCES [KORISNIK]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZAHTEV_PREVOZ]
	ADD  FOREIGN KEY ([IdP]) REFERENCES [PAKET]([IdP])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZAHTEV_PREVOZ]
	ADD  FOREIGN KEY ([OpstinaOd]) REFERENCES [OPSTINA]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZAHTEV_PREVOZ]
	ADD  FOREIGN KEY ([OpstinaDo]) REFERENCES [OPSTINA]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
