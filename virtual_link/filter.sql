t.row_id in (
	select trn_order.trn_ord_cli_id from trn_order
	left join trn_product on trn_product.row_id=trn_order.trn_ord_prd_id
	where trn_product.trn_prd_sup_id=[row_id]
)
