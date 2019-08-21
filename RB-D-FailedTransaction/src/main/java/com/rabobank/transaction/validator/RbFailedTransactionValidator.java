package com.rabobank.transaction.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabobank.transaction.aggregator.RbFailedTransactionAggregator;
import com.rabobank.transaction.model.CustomerTransaction;
import com.rabobank.transaction.model.FailedTransaction;
import com.rabobank.transaction.util.Constants;

@Component
public class RbFailedTransactionValidator {

	private static final Logger log = LoggerFactory.getLogger(RbFailedTransactionValidator.class);

	public static final String CLASS_NAME = RbFailedTransactionAggregator.class.getName();

	/**
	 * This method will validate transactions to find failed
	 * 
	 * @author Muhammad Imran
	 * @param List<CustomerTransactions>
	 * @return List<FailedTransaction>
	 * @throws Exception
	 */
	public List<FailedTransaction> validateTransactions(List<CustomerTransaction> customerTransactions)
			throws Exception {

		String function = CLASS_NAME + ".validateTransactions-->";

		log.info(Constants.METHOD_STARTS + function);

		List<FailedTransaction> failedTransactionsList = new ArrayList<FailedTransaction>();

		try {

			Map<Integer, String> failedTransactionMap = new HashMap<Integer, String>();

			if (null != customerTransactions && customerTransactions.size() > 0) {

				for (CustomerTransaction customerTransaction : customerTransactions) {

					FailedTransaction failedTransaction = new FailedTransaction();

					if (failedTransactionMap.containsKey(customerTransaction.getReference())) {

						failedTransaction.setReference(customerTransaction.getReference());
						failedTransaction.setDescription(customerTransaction.getDescription());
						failedTransactionsList.add(failedTransaction);
					}

					else {

						BigDecimal totalBalance = new BigDecimal(customerTransaction.getStartBalance())
								.add(new BigDecimal(customerTransaction.getMutation()));

						if (!totalBalance.equals(new BigDecimal(customerTransaction.getEndBalance()))) {
							failedTransaction.setReference(customerTransaction.getReference());
							failedTransaction.setDescription(customerTransaction.getDescription());
							failedTransactionsList.add(failedTransaction);
						}
						failedTransactionMap.put(customerTransaction.getReference(),
								customerTransaction.getDescription());
					}
				}
			}
		} catch (Exception e) {

			log.error(Constants.EXCEPTION_OCCURRED + function + e.getMessage());
			throw new Exception(e.getMessage());
		}

		log.info(Constants.METHOD_ENDS + function);

		return failedTransactionsList;
	}
}
