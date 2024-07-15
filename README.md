# Bangladesh MP Election eVote App.

I developed this App mainly using the Java programming language. This app is the "MP Election app of Bangladesh". After entering the app, scan your National Digital ID card, select the area, and submit, the candidates in that area will automatically appear. Here, when the ID card is scanned and submitted, the ID card will be verified whether the user has voted before and whether his ID card is valid. Then the user can easily select the candidate of his choice and vote. Here is another option "Hide Me", if the user clicks on it and votes then no information will go to the admin. User's face verification will be done while voting.

An admin app has been created to control this voting application, within which the admin can add, edit, and delete districts and areas and add, edit, and delete candidates in that area. Admin app has another section View Results. Here admin can see how many votes the candidates got in that area by selecting the district and area. And the admin can easily see the details of the users who have voted for those candidates.

# The technology I used to develop this App.

<ol>
    <li>
        I used Google Firebase cloud text recognition service to convert text from an ID card. Collected Bengali and English text from NID card images through this Firebase ML text recognition.
    </li>
    <li>
        I used the machine learning OpenCV library to process the image and clear it. So that the texts can be easily detected.
    </li>
    <li>
        I have used Google Firebase FireStore and Real-Time Database to save user data.
    </li>
    <li>
        I used the "fotoapparat" camera library to capture the image.
    </li>
    <li>
        Used Glide library to load images and SSP and SDP libraries to make the app responsive.
    </li>
    <li>
        Also, I used the Volley library, to receive data from API.
    </li>
</ol>

## Screenshots

<div style="display:flex">
    <img src="https://media.licdn.com/dms/image/D5622AQFyzNzpCJ-2dw/feedshare-shrink_800/0/1701354011636?e=1723680000&v=beta&t=Lp0Hw-zmKbc7xVY7YIaAevHOMxBP7Lguf9r5hsOT9d8" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQHZN9MOy6shlQ/feedshare-shrink_800/0/1701354014774?e=1723680000&v=beta&t=whVEZJBPjZCJJGWpCfmZZDlnrPGiBoHqr7m0dS-KPDI" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQG52_CE235JDQ/feedshare-shrink_800/0/1701354012327?e=1723680000&v=beta&t=xEfD-e7p5-GjNXGz5oi3kE-0mwBKUPGGGCWtp_oY7nc" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQFtqrvUSjHX8w/feedshare-shrink_800/0/1701354007422?e=1723680000&v=beta&t=5ER3p8Gr35GPbjD-yBSCX0QvdYHX7LB4ReCkWZT302s" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQEufSSbChQ0Tg/feedshare-shrink_800/0/1701354013431?e=1723680000&v=beta&t=7r7mvk_68tE8rLeDDE4l5rw0nKgrM15E_fS1_tVUB2U" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQG45adbSwkmtQ/feedshare-shrink_800/0/1701354006745?e=1723680000&v=beta&t=r-2PhvywgnAB1ebQLDyPjRQ1Kg9NWNc1B2rEfasj-FQ" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQHow0igCu_4fw/feedshare-shrink_800/0/1701354014686?e=1723680000&v=beta&t=4Q-Uk68NebirsshDznWXHduj5H9FkvBjVqiYf2us3kI" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQEYzmzgfv4UUQ/feedshare-shrink_800/0/1701354014814?e=1723680000&v=beta&t=-vlrhSMDtesa9JYl0VzOnlMXLijg1vHYAzW5ZnzUUBU" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQGz3Ji3fkH1TA/feedshare-shrink_800/0/1701354016049?e=1723680000&v=beta&t=EKKXx-BA4kVVJCqEMDo98LWNi142FOGds2yfRETp4u0" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQFF0isWvnxrDA/feedshare-shrink_800/0/1701354013867?e=1723680000&v=beta&t=hi5hWtUpF10KFTnbifO09miM_NhJw2HpEDj8omldhbw" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQGMN_xnAoVtQA/feedshare-shrink_800/0/1701354011069?e=1723680000&v=beta&t=YLVNT3s6YEzQwgvrZBqFGrEp4m8w3mrExIsekKBuAxg" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQGpu2vyE55viA/feedshare-shrink_800/0/1701354007345?e=1723680000&v=beta&t=a-wTauqmsDvhcGdk8auS9xps_TX1DSIvdTVAAgLK2nc" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQEBGjSmBOEFxQ/feedshare-shrink_800/0/1701354012262?e=1723680000&v=beta&t=pwYbCs8HyVjQEo0r9uwzm2sOJV5XQzDiXqBFHGU9PE4" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQEQhTOEPn3Esw/feedshare-shrink_800/0/1701354015306?e=1723680000&v=beta&t=Es7nDdgyEFuSlaR6STfMvVfJlYH-gMmk7ZFGWt3gayY" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQErsyg-JdR0Dw/feedshare-shrink_800/0/1701354013260?e=1723680000&v=beta&t=I3ODPyU9ly8mHnUTLw-u4CKWmo3Atq1g4f-Mm0ZlAC0" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQHcoVUiamUEbg/feedshare-shrink_800/0/1701354013109?e=1723680000&v=beta&t=w4vpkPyRlAVRej0Paj2PvYJSUTzoffNs1WBPbVi_H6E" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
    <img src="https://media.licdn.com/dms/image/D5622AQFEkVUkRVOkMQ/feedshare-shrink_800/0/1701354011661?e=1723680000&v=beta&t=NMbBsvjVpu3-PFVV9O46Bh4vAl0l4x9aWcPmahOmDIw" alt="Home Screen" width="150" height="300" style="margin-right: 30px;">
</div>

