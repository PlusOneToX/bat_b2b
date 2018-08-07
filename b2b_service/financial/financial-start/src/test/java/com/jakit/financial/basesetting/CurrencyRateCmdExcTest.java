package com.bat.financial.basesetting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;

import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.financial.DistributorApplicationTests;
import com.bat.financial.basesetting.executor.CurrencyRateCmdExc;
import com.bat.financial.refund.RefundServiceImpl;

public class CurrencyRateCmdExcTest extends DistributorApplicationTests {

    @Resource
    private CurrencyRateCmdExc currencyRateCmdExc;

    @Resource
    private RefundServiceImpl refundService;

    @Test
    public void testSyncCurrencyRate() {
        String a =
            "229112, 6.5079, 0.1537, USD, CNY, Mon Jan 01 00:00:00 CST 2018, Wed Feb 28 00:00:00 CST 2018&229113, 6.3352, 0.1578, USD, CNY, Thu Mar 01 00:00:00 CST 2018, Sat Mar 31 00:00:00 CST 2018&229114, 6.2764, 0.1593, USD, CNY, Sun Apr 01 00:00:00 CST 2018, Mon Apr 30 00:00:00 CST 2018&229115, 6.367, 0.1571, USD, CNY, Tue May 01 00:00:00 CST 2018, Thu May 31 00:00:00 CST 2018&229116, 6.8293, 0.1464, USD, CNY, Wed Aug 01 00:00:00 CST 2018, Fri Aug 31 00:00:00 CST 2018&229117, 6.8347, 0.1463, USD, CNY, Sat Sep 01 00:00:00 CST 2018, Sun Sep 30 00:00:00 CST 2018&229118, 6.8957, 0.145, USD, CNY, Mon Oct 01 00:00:00 CST 2018, Wed Oct 31 00:00:00 CST 2018&229119, 6.967, 0.1435, USD, CNY, Thu Nov 01 00:00:00 CST 2018, Fri Nov 30 00:00:00 CST 2018&229120, 6.9431, 0.144, USD, CNY, Sat Dec 01 00:00:00 CST 2018, Mon Dec 31 00:00:00 CST 2018&229121, 6.4078, 0.1561, USD, CNY, Fri Jun 01 00:00:00 CST 2018, Sat Jun 30 00:00:00 CST 2018&229122, 6.6157, 0.1512, USD, CNY, Sun Jul 01 00:00:00 CST 2018, Tue Jul 31 00:00:00 CST 2018&241033, 6.8482, 0.146, USD, CNY, Tue Jan 01 00:00:00 CST 2018, Thu Jan 31 00:00:00 CST 2018&765823, 6.7081, 0.1491, USD, CNY, Fri Feb 01 00:00:00 CST 2018, Thu Feb 28 00:00:00 CST 2018&929110, 6.6957, 0.1493, USD, CNY, Fri Mar 01 00:00:00 CST 2018, Sun Mar 31 00:00:00 CST 2018&1007398, 6.7193, 0.1488, USD, CNY, Mon Apr 01 00:00:00 CST 2018, Tue Apr 30 00:00:00 CST 2018&1007400, 6.7344, 0.1485, USD, CNY, Wed May 01 00:00:00 CST 2018, Fri May 31 00:00:00 CST 2018&1076239, 6.8896, 0.1451, USD, CNY, Sat Jun 01 00:00:00 CST 2018, Sun Jun 30 00:00:00 CST 2018&1380953, 6.8716, 0.1455, USD, CNY, Mon Jul 01 00:00:00 CST 2018, Wed Jul 31 00:00:00 CST 2018&1380954, 6.8938, 0.1451, USD, CNY, Thu Aug 01 00:00:00 CST 2018, Sat Aug 31 00:00:00 CST 2018&1525366, 7.0883, 0.1411, USD, CNY, Sun Sep 01 00:00:00 CST 2018, Mon Sep 30 00:00:00 CST 2018&1593697, 7.0726, 0.1414, USD, CNY, Tue Oct 01 00:00:00 CST 2018, Thu Oct 31 00:00:00 CST 2018&1640365, 7.0437, 0.142, USD, CNY, Fri Nov 01 00:00:00 CST 2018, Sat Nov 30 00:00:00 CST 2018&1689361, 7.0262, 0.1423, USD, CNY, Sun Dec 01 00:00:00 CST 2018, Tue Dec 31 00:00:00 CST 2018&1726717, 6.9614, 0.1436, USD, CNY, Wed Jan 01 00:00:00 CST 2018, Fri Jan 31 00:00:00 CST 2018&1803585, 6.9249, 0.1444, USD, CNY, Sat Feb 01 00:00:00 CST 2018, Sat Feb 29 00:00:00 CST 2018&1840369, 6.9811, 0.1432, USD, CNY, Sun Mar 01 00:00:00 CST 2018, Tue Mar 31 00:00:00 CST 2018&2095425, 7.0771, 0.1413, USD, CNY, Wed Apr 01 00:00:00 CST 2018, Thu Apr 30 00:00:00 CST 2018&2261176, 7.069, 0.1415, USD, CNY, Fri May 01 00:00:00 CST 2018, Sun May 31 00:00:00 CST 2018&2497173, 7.1315, 0.1402, USD, CNY, Mon Jun 01 00:00:00 CST 2018, Tue Jun 30 00:00:00 CST 2018&2835481, 7.071, 0.1414, USD, CNY, Wed Jul 01 00:00:00 CST 2018, Fri Jul 31 00:00:00 CST 2018&3075137, 6.998, 0.1429, USD, CNY, Sat Aug 01 00:00:00 CST 2018, Mon Aug 31 00:00:00 CST 2018&3293951, 6.8498, 0.146, USD, CNY, Tue Sep 01 00:00:00 CST 2018, Wed Sep 30 00:00:00 CST 2018&3591542, 6.7796, 0.1475, USD, CNY, Thu Oct 01 00:00:00 CST 2018, Sat Oct 31 00:00:00 CST 2018&3823065, 6.705, 0.1491, USD, CNY, Sun Nov 01 00:00:00 CST 2018, Mon Nov 30 00:00:00 CST 2018&4092925, 6.5921, 0.1517, USD, CNY, Tue Dec 01 00:00:00 CST 2018, Thu Dec 31 00:00:00 CST 2018&4320163, 6.5408, 0.1529, USD, CNY, Fri Jan 01 00:00:00 CST 2018, Sun Jan 31 00:00:00 CST 2018&4557290, 6.4623, 0.1547, USD, CNY, Mon Feb 01 00:00:00 CST 2018, Sun Feb 28 00:00:00 CST 2018&4838514, 6.4754, 0.1544, USD, CNY, Mon Mar 01 00:00:00 CST 2018, Wed Mar 31 00:00:00 CST 2018&5099937, 6.5584, 0.1525, USD, CNY, Thu Apr 01 00:00:00 CST 2018, Fri Apr 30 00:00:00 CST 2018&5256786, 6.4895, 0.1541, USD, CNY, Sat May 01 00:00:00 CST 2018, Mon May 31 00:00:00 CST 2018&5442537, 6.3572, 0.1573, USD, CNY, Tue Jun 01 00:00:00 CST 2018, Wed Jun 30 00:00:00 CST 2018&5608514, 6.4709, 0.1545, USD, CNY, Thu Jul 01 00:00:00 CST 2018, Sat Jul 31 00:00:00 CST 2018&5873430, 6.466, 0.1547, USD, CNY, Sun Aug 01 00:00:00 CST 2018, Tue Aug 31 00:00:00 CST 2018";
        String[] split = a.split("&");
        SimpleDateFormat sdf = new SimpleDateFormat(" EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        List<CurrencyRateErpRpcDTO> collect = Arrays.stream(split).map(s -> {
            CurrencyRateErpRpcDTO dto = new CurrencyRateErpRpcDTO();
            String[] split1 = s.split(",");
            dto.setRateId(Integer.valueOf(split1[0]));
            dto.setExchangeRate(Double.valueOf(split1[1]));
            dto.setReverseExRate(Double.valueOf(split1[2]));
            dto.setCyForid(split1[3]);
            dto.setCyToid(split1[4]);
            try {
                dto.setBegDate(sdf.parse(split1[5]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                dto.setEndDate(sdf.parse(split1[6]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dto;
        }).collect(Collectors.toList());
        currencyRateCmdExc.syncCurrencyRate(collect);
    }

}