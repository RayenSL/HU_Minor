INSERT INTO account ( id, iban, balance, active )
VALUES
( 'a1e4107e-cb24-4c66-8e73-b7b2a2870987', 'NL51INGB0000123456', '100.00', true ),
( '7aba18a5-2528-4c60-913d-e091fcfefede', 'NL37ABNA9999888877', '1000000.00', true );

INSERT INTO holder ( id, bsn, name )
VALUES
( 'f848b5a7-6460-44e7-8464-051b8c96626b', '123456789', 'A Wassink' ),
( '4f8a22f4-7c70-4969-b5fb-3326309d49cb', '987654321', 'S O meone' );

INSERT INTO account_holder ( account_id, holder_id )
VALUES ( '7aba18a5-2528-4c60-913d-e091fcfefede', 'f848b5a7-6460-44e7-8464-051b8c96626b' );