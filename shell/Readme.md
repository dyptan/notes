Download patch directly to the server
`sftp -P 115 ivand@sftp.mapr.com -D /servdata/support/maprpatches/v6.1.0/rpm/mapr-patch-6.1.0.20180926230239.GA-20211026074111.x86_64.rpm`
Download directory
`sshpass -p 'MyPassword' sftp -P 115 -o StrictHostKeyChecking=no -r ivand@sftp.mapr.com:/servdata/support/ivand`
