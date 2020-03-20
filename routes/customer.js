

module.exports = {
    deleteCustomer: (req, res) => {
        let Id = req.params.barcode;
        let deleteUserQuery = 'DELETE FROM customerinfo WHERE barcode = "' + Id + '"';

                db.query(deleteUserQuery, (err, result) => {
                    if (err) {
                        return res.status(500).send(err);
                    }
                    res.redirect('/');
                });
            }
        }