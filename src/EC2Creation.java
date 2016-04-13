/*******************************************************************************************
							Create an amazon EC2 Client
*******************************************************************************************/

			/********************************************************************************************************************
			1. Create and initialize an AWSCredentials instance. Specify the AwsCredentials.properties file you created, as follows:
			********************************************************************************************************************/

					AWSCredentials credentials = new PropertiesCredentials(
					AwsConsoleApp.class.getResourceAsStream("AwsCredentials.properties"));   //properties file contains accessid and secret access key
			/********************************************************************************************************************
			2. Use the AWSCredentials object to create a new AmazonEC2Client instance, as follows:
			********************************************************************************************************************/
					amazonEC2Client = new AmazonEC2Client(credentials);
			/***************************************************************************************************************************************
			3. By default, the service endpoint is ec2.us-east-1.amazonaws.com. To specify a different endpoint, use the setEndpoint method. For example:
			********************************************************************************************************************/
					amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com");
					
/*******************************************************************************************
							Create an Amazon EC2 Security Group
*******************************************************************************************/
			
			/********************************************************************************************************************
			1. Create and initialize a CreateSecurityGroupRequest instance. Use the withGroupName method to set the security group
			   name, and the withDescription method to set the security group description, as follows:
			********************************************************************************************************************/
				CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
				csgr.withGroupName("JavaSecurityGroup").withDescription("My security group");
				
				/*
				The security group name must be unique within the AWS region in which you initialize your Amazon EC2 client.
				You must use US-ASCII characters for the security group name and description.
				*/
			/********************************************************************************************************************
			2. Pass the request object as a parameter to the createSecurityGroup method. The method returns a CreateSecurityGroupResult
			object, as follows:
			********************************************************************************************************************/
					CreateSecurityGroupResult createSecurityGroupResult =
					amazonEC2Client.createSecurityGroup(createSecurityGroupRequest);  //csgr
			/*******************************************************************************************************************/
			
					/*
					To authorize security group ingress :
					----------------------------------------------------------------------------------------------------------------------
					1. Create and initialize an IpPermission instance. Use the withIpRanges method to set the range of IP addresses 
					   to authorize ingress for, and use the withIpProtocol method to set the IP protocol. Use the withFromPort and 
					   withToPort methods to specify range of ports to authorize ingress for, as follows:
 
						IpPermission ipPermission = new IpPermission();
						ipPermission.withIpRanges("111.111.111.111/32", "150.150.150.150/32")
																	.withIpProtocol("tcp")
																	.withFromPort(22)
																	.withToPort(22);
					
					
					  All the conditions that you specify in the IpPermission object must be met in order for ingress to be allowed.
					  Specify the IP address using CIDR notation. If you specify the protocol as TCP/UDP, you must provide a source port
					  and a destination port. You can authorize ports only if you specify TCP or UDP.
                    -----------------------------------------------------------------------------------------------------------------------
					2. Create and initialize an AuthorizeSecurityGroupIngressRequest instance. Use the withGroupName method to specify the 
					   security group name, and pass the IpPermission object you initialized earlier to the withIpPermissions method, as follows:

						AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
						new AuthorizeSecurityGroupIngressRequest();
						authorizeSecurityGroupIngressRequest.withGroupName("JavaSecurityGroup")
                                    .withIpPermissions(ipPermission);
					-------------------------------------------------------------------------------------------------------------------------				
					3.Pass the request object into the authorizeSecurityGroupIngress method, as follows:
						amazonEC2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
						
					If you call authorizeSecurityGroupIngress with IP addresses for which ingress is already authorized, the method throws an 
					exception. Create and initialize a new IpPermission object to authorize ingress for different IPs, ports, and protocols before
					calling AuthorizeSecurityGroupIngress.
                    */
/*******************************************************************************************
							Create a Key Pair
*******************************************************************************************/		
				/*
				To create a key pair and save the private key :
				1. Create and initialize a CreateKeyPairRequest instance. Use the withKeyName method to set the key pair name, as follows:
							CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();	
							createKeyPairRequest.withKeyName(keyName);
                2. Pass the request object to the createKeyPair method. The method returns a CreateKeyPairResult instance, as follows:
							CreateKeyPairResult createKeyPairResult =
																	amazonEC2Client.createKeyPair(createKeyPairRequest);
			    3. Call the result object’s getKeyPair method to obtain a KeyPair object. Call the KeyPair object’s getKeyMaterial method to 
					obtain the unencrypted PEM-encoded private key, as follows:
					KeyPair keyPair = new KeyPair();
					keyPair = createKeyPairResult.getKeyPair();
                    String privateKey = keyPair.getKeyMaterial();
					
				*/
/*******************************************************************************************
							Run an amazon ec2 instance
*******************************************************************************************/
				/*
				1. Create and initialize a RunInstancesRequest instance. Make sure that the AMI, key pair, and security group that you specify
				exist in the region that you specified when you created the client object.
				---------------------------------------------------------------------------------------
						RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
						runInstancesRequest.withImageId("ami-4b814f22")
														.withInstanceType("m1.small")
														.withMinCount(1)
														.withMaxCount(1)
														.withKeyName("my-key-pair")
														.withSecurityGroups("my-security-group");
				----------------------------------------------------------------------------------------
				2.Launch the instances by passing the request object to the runInstances method. The method returns a RunInstancesResult object,
					as follows:
				--------------------------------------------------------------------------------------------
						RunInstancesResult runInstancesResult =
														amazonEC2Client.runInstances(runInstancesRequest);