//
//  BookViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/4/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "BookViewController.h"

@interface BookViewController ()
@property (nonatomic, weak) NSMutableData *responseData;

@property (nonatomic, strong) NSMutableArray *timeSlots;

@property (nonatomic, strong) IBOutlet UIPickerView *pickerView;
@end

@implementation BookViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    //NSLog(@"%@", self.room.name);
    self.timeSlots = [[NSMutableArray alloc] initWithObjects:@"9-10", @"10-11", @"11-12", @"12-13", @"13-14", @"14-15", @"15-16", @"16-17", nil];
    
    NSDate *today = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:today];
    self.dateTF.text = [NSString stringWithFormat:@"%@", dateString];
    [self getAvailableTimeSlotsForTheDate:dateString];
    
    UIDatePicker *datePicker = [[UIDatePicker alloc] init];
    datePicker.datePickerMode = UIDatePickerModeDate;
    datePicker.minuteInterval = 30;
    [datePicker addTarget:self action:@selector(updateTextField:) forControlEvents:UIControlEventValueChanged];
    [self.dateTF setInputView:datePicker];
    

    self.pickerView = [[UIPickerView alloc] init];
    self.pickerView.dataSource = self;
    self.pickerView.delegate = self;
    [self.timeTF setInputView:self.pickerView];
}

- (NSArray *)getAvailableTimeSlotsForTheDate: (NSString *)dateString {
    
    NSString *url = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking/%@", self.room.name, dateString];
    NSURL *jsonURL = [NSURL URLWithString:url];
    NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
    
    NSArray *jsonDict = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    
    for (NSDictionary *bookingDict in jsonDict) {
        NSString *startTime = [bookingDict objectForKey:@"StartT"];
        NSString *endTime = [bookingDict objectForKey:@"EndT"];
        NSString *timeSlot = [NSString stringWithFormat:@"%@-%@", startTime, endTime];
        //NSLog(@"%@", timeSlot);
        [self.timeSlots removeObject:timeSlot];
    }
    
    //NSMutableArray *availableTimeSlots = [NSMutableArray array];
    
    return self.timeSlots;
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.timeSlots.count;
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [self.timeSlots objectAtIndex:row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    //NSLog(@"selected row %d", row);
    self.timeTF.text = [self.timeSlots objectAtIndex:row];
}

-(void)updateTextField:(id)sender {
    UIDatePicker *datePicker = (UIDatePicker *)self.dateTF.inputView;
    NSDate *selectedDate = datePicker.date;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *newDate = [dateFormatter stringFromDate:selectedDate];
    self.dateTF.text = [NSString stringWithFormat:@"%@", newDate];
    [self getAvailableTimeSlotsForTheDate:newDate];
    [self.pickerView reloadAllComponents];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)cancel:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (IBAction)saveTheBooking:(id)sender {
    NSString *urlString = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking", self.room.name];
    NSURL *url = [NSURL URLWithString:urlString];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:@"POST"];
    NSString *date = self.dateTF.text;
    NSString *timeslot = self.timeTF.text;
    NSArray *timeArray = [timeslot componentsSeparatedByString:@"-"];
    NSString *startTime = timeArray[0];
    NSString *endTime = timeArray[1];
    NSString *meetingName = self.meetingNameTF.text;
    NSString *users = self.usersTF.text;
    NSString *bodyString = [NSString stringWithFormat:@"date=%@&startTime=%@&endTime=%@&meetingName=%@&users=%@",
                            date, startTime, endTime, meetingName, users];
    [request setHTTPBody:[bodyString dataUsingEncoding:NSUTF8StringEncoding]];
    
//    NSURLResponse *response;
//    NSError *error;
    
    //NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    
    if (connection) {
        NSLog(@"connecting......");
        self.responseData = [NSMutableData data];
    }
    //NSLog(@"%@", [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
    //[connection start];
}

-(void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
}

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [self.responseData appendData:data];
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSString *responseString = [[NSString alloc] initWithData:self.responseData encoding:NSUTF8StringEncoding];
    
    NSLog(@"finished: %@", responseString);
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
